package org.tala.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Simon on 14/02/2017.
 */
@SpringBootApplication(scanBasePackages = { "org.tala.bank" })
public class Application
{

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
