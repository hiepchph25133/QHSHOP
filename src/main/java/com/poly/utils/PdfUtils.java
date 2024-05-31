package com.poly.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

import java.util.Objects;

public class PdfUtils {
    public static PdfPCell createCell(String value , Font font){
        value = Objects.isNull(value)? "" :value;
        PdfPCell cell = (!value.isBlank()) ? new PdfPCell(new Paragraph(value,font)) : new PdfPCell();
        cell.setBorderColor(BaseColor.BLACK);
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.WHITE);
        if (value.isBlank()) cell.setBorderWidthRight(0);
        cell.setExtraParagraphSpace(5f);
        return cell;
    }
}
