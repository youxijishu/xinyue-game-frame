package com.xinyue.gateway.balance.controller;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.xinyue.gateway.balance.config.GatewayServerInfo;

//如果是web接口测试，必须添加随机端口
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class GatewayBalanceControllerTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void selectGateway() {
		GatewayServerInfo gatewayServerInfo = testRestTemplate.getForObject("/gateway/select/1001",
				GatewayServerInfo.class);
		assertNotNull(gatewayServerInfo);
		assertEquals(8089, gatewayServerInfo.getPort());
		System.out.println("选择的gateway:" + gatewayServerInfo);
	}
}
