package com.mli.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author baisakhi
 *
 */
public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private PrintWriter writer = new PrintWriter(outputStream);

	public BufferedServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {

		return new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {

				outputStream.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				outputStream.write(b);
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener listener) {

			}
		};
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		} else if (outputStream != null) {
			outputStream.flush();
		}
	}

	public String getResponseData() {
		return outputStream.toString();
	}
	

}
