package com.erocha.playground.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import com.erocha.playground.model.InputJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMimeKeys;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.DcXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

@RestController
@RequestMapping(value = "/binary")
@Slf4j
public class BinaryController {

	@PostMapping
	@RequestMapping(value = "/file", consumes = "image/svg+xml")
	public void addBinary(@RequestBody byte[] input) {
		InputStream inputStream = new ByteArrayInputStream(input);
		File file = new File("image.svg");
		try {
			FileUtils.writeByteArrayToFile(file, input);
			validateMimeType(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void validateMimeType(InputStream inputStream) {
		try {
			Parser parser = new DcXMLParser();
			Metadata metadata = new Metadata();
			parser.parse(inputStream, new BodyContentHandler(), metadata, new ParseContext());
			String mediaType = metadata.get(Metadata.CONTENT_TYPE);
			log.info(mediaType);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}

	@PostMapping
	@RequestMapping(value = "/base64", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void base64(@RequestBody InputJson inputJson) {
		byte[] base64 = Base64.getMimeDecoder().decode(inputJson.getBody());
		InputStream inputStream = new ByteArrayInputStream(base64);
		validateMimeType(inputStream);
	}

}
