package com.interconn.demo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于封装控制层（Controller与客户端交互的数据信息） 通过重载有参构造函数，可以选择不同类型的封装方法
 *
 * @author zgl
 *
 */

@Getter
@Setter
public class JSONResponse {

	private Integer state;//状态码信息： state = 1 表示成功 state = 0 表示失败
	private String message;// 状态码对应的具体信息
	private Object data;// 封装具体数据对象

	public JSONResponse() {
	}

	public JSONResponse(Integer state, String message, Object data) {
		super();
		this.state = state;
		this.message = message;
		this.data = data;
	}

	public JSONResponse(Integer state, Object data) {
		super();
		this.state = state;
		this.data = data;
	}

	public JSONResponse(Integer state, String message) {
		super();
		this.state = state;
		this.message = message;
	}


}
