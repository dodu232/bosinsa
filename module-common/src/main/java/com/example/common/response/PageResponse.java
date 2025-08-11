package com.example.common.response;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResponse<T> {

	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;

	private PageResponse(List<T> content, int page, int size, long totalElements) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;

		this.totalPages = (int) ((totalElements + size - 1) / size);
	}

	public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
		return new PageResponse<>(content, page, size, totalElements);
	}
}
