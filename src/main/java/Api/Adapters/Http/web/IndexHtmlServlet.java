/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Api.Adapters.Http.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class IndexHtmlServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(IndexHtmlServlet.class);
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    try {
      URL indexResource = getServletContext().getResource("/index.html");
      String content = IOUtils.toString(indexResource, StandardCharsets.UTF_8);

      resp.setContentType("text/html");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().append(content);
    } catch (IOException e) {
      LOGGER.error("Error rendering index.html.", e);
    }
  }
}
