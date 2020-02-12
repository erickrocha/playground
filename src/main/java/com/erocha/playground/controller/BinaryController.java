package com.erocha.playground.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.erocha.playground.model.InputJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.DcXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
			Tika tika = new Tika();
			String mimeType = tika.detect(file);
			log.info(mimeType);
			Path path = file.toPath();
			mimeType = Files.probeContentType(path);
			log.info(mimeType);
			validateMimeType(inputStream);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
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
			log.error(e.getMessage(), e);
		} catch (SAXException e) {
			log.error(e.getMessage(), e);
		} catch (TikaException e) {
			log.error(e.getMessage(), e);

		}
	}

	@PostMapping
	@RequestMapping(value = "/base64", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void base64(@RequestBody InputJson inputJson) {
		byte[] base64 = Base64.getMimeDecoder().decode(inputJson.getBody());
		InputStream inputStream = new ByteArrayInputStream(base64);
		File file = new File("image.svg");
		try {
			FileUtils.writeByteArrayToFile(file, base64);
			Tika tika = new Tika();
			String mimeType = tika.detect(file);
			log.info(mimeType);
			Path path = file.toPath();
			mimeType = Files.probeContentType(path);
			log.info(mimeType);
			validateMimeType(inputStream);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@PostMapping
	@RequestMapping(value = "/multipart")
	public void multipart(@RequestParam("file") final MultipartFile upload) {
		File file = new File("image.svg");
		try {
			FileUtils.writeByteArrayToFile(file, upload.getBytes());
			Tika tika = new Tika();
			String mimeType = tika.detect(file);
			log.info(mimeType);
			Path path = file.toPath();
			mimeType = Files.probeContentType(path);
			log.info(mimeType);
			validateMimeType(upload.getInputStream());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
