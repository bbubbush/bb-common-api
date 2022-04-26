package com.bb.common.util;

import com.bb.common.vo.common.ResponseVO;

public class ApiResponse<T> extends ResponseVO<T> {
  public static <T> ResponseVO<T> of(T body) {
    return ResponseVO.success(body);
  }
}
