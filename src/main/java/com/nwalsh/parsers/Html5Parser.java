package com.nwalsh.parsers;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Html5Parser {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: html5parser URI");
            System.exit(0);
        }

        try {
            URL url = new URL(args[0]);
            URLConnection conn = url.openConnection();
            HtmlDocumentBuilder htmlBuilder = new HtmlDocumentBuilder(XmlViolationPolicy.ALTER_INFOSET);
            Document html = htmlBuilder.parse(conn.getInputStream());

            Processor processor = new Processor(false);
            DocumentBuilder builder = processor.newDocumentBuilder();
            XdmNode doc = builder.build(new DOMSource(html));

            Serializer serializer = processor.newSerializer(System.out);
            serializer.setOutputProperty(new QName("", "method"), "xhtml");
            serializer.setOutputProperty(new QName("", "omit-xml-declaration"), "yes");
            serializer.setOutputProperty(new QName("", "indent"), "no");
            serializer.serializeXdmValue(doc);

        } catch (IOException | SAXException | SaxonApiException mue) {
            throw new RuntimeException(mue);
        }

    }
}
