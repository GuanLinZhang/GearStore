package com.interconn.demo.vo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
/***
 * 通过使用JasonSerializer注解，显示时间类型的数据。
 * 此注解需要备注在实体类的get（TIme）方法上。
 * @author Yang
 *
 */
public class DataJsonSerializer extends JsonSerializer{

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		 String str = sdf.format(value);
		 gen.writeString(str);
	}

}
