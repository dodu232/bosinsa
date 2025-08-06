package com.example.common.response;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResponse<T> {

	private List<T> content;
	private int page;
	private int size;
	private int totalElements;
	private int totalPages;

	private PageResponse(List<T> content, int page, int size, int totalElements) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;

		this.totalPages = (int) ((totalElements + size - 1) / size);
	}

	public static <T> PageResponse<T> of(List<T> content, int page, int size, int totalElements) {
		return new PageResponse<>(content, page, size, totalElements);
	}
}
