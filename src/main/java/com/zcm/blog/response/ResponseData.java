package com.zcm.blog.response;

import lombok.Data;

/**
 * @author sijia.zhang
 * @date 2021-11-19 19:19:28
 */
@Data
public class ResponseData {

	private int status;

	private String message;

	private Object data;

	public ResponseData(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public ResponseData(int status, Object data) {
		this.status = status;
		this.data = data;
	}
}
