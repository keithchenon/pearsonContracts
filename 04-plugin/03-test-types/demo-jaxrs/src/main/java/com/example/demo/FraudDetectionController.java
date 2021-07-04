package com.example.demo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.example.demo.model.FraudCheck;
import com.example.demo.model.FraudCheckResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.demo.model.FraudCheckStatus.NOT_OK;
import static com.example.demo.model.FraudCheckStatus.OK;

@Controller
@Path("/")
public class FraudDetectionController {

	@POST
	@Path("/check")
	@Produces("application/json")
	@Consumes("application/json")
	public FraudCheckResult fraudCheck(@RequestBody(required = false) FraudCheck fraudCheck) {
		if (fraudCheck.getAge() >= 20) {
			return new FraudCheckResult(OK);
		}
		return new FraudCheckResult(NOT_OK);
	}

}
