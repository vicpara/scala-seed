package com.vicpara.certa

import java.io._

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument

import scala.collection.JavaConverters._

case object IO {

  def fileToText(f: File): String =
    f.getAbsolutePath.split("[.]").last match {
      case "docx" => IO.readDocx(f)
      case "doc" => IO.readDoc(f)
      case "pdf" => IO.readPdf(f)
    }

  def readPdf(f: File): String = {
    val pdfStripper = new PDFTextStripper
    val pdDoc = PDDocument.load(f)
    pdfStripper.setStartPage(1)
    pdfStripper.setEndPage(5)
    pdfStripper.getText(pdDoc)
  }

  def readDocx(f: File): String = {
    val fis = new FileInputStream(f)
    val xdoc = new XWPFDocument(OPCPackage.open(fis))
    new XWPFWordExtractor(xdoc).getText
  }

  def readDoc(f: File): String = {
    val fis = new FileInputStream(f)
    val doc = new HWPFDocument(fis)
    doc.getDocumentText
  }

  def saveToFile(fname: String, append: Boolean = false)(text: String): Unit = {
    val bw = new BufferedWriter(new FileWriter(fname, append))
    bw.write(text)
    bw.flush()
    bw.close()
  }

  def read(filename: String): Seq[DataClass] = {
    val myFile = new File(filename)
    val fis = new FileInputStream(myFile)
    val myWorkBook = new XSSFWorkbook(fis)
    val mySheet = myWorkBook.getSheetAt(0)

    mySheet.iterator().asScala.dropWhile(r => r.getCell(3) == null).drop(1).map(r => {
      DataClass(
        docId = r.getCell(1).getNumericCellValue.toInt,
        hasDef = r.getCell(2).getStringCellValue.equals("Yes"),
        defVariant = r.getCell(3).getStringCellValue,
        hasColon = r.getCell(4).getStringCellValue.equals("Yes"),
        infoContext = r.getCell(5).getStringCellValue
      )
    }).toSeq
  }

  def textToXLSX(data: List[(String, String)]): Unit = {
    val wb = new XSSFWorkbook()
    val creationHelper = wb.getCreationHelper
    val sheet = wb.createSheet("Exported Data")
    val header = sheet.createRow(0)

    header.createCell(0).setCellValue("Filename")
    header.createCell(1).setCellValue("Raw Text")

    data.zipWithIndex.foreach(r => {
      val row = sheet.createRow(r._2 + 1)

      val filename = row.createCell(0)
      filename.setCellValue(r._1._1)

      val text = row.createCell(1)
      text.setCellValue(r._1._2.take(1300) + "...")
    })

    val fileOut = new FileOutputStream("RawText.out.xlsx")
    wb.write(fileOut)
    fileOut.close()
    wb.close()
    fileOut.close()
    println(s"Output in ${
      "RawText.out.xlsx"
    }")
  }

}