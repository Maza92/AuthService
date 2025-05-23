package com.auth.authservice.service.email;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String generateEmailContent(String template, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);

        return templateEngine.process(template, context);
    }
}