window.onload = function() {
  window.ui = SwaggerUIBundle({
    url: "/api/openapi.json",  // Your actual spec path
    dom_id: '#swagger-ui',
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    layout: "StandaloneLayout",
    validatorUrl: null
  });
};
