package com.hyperativa.services.card.dto;

import java.util.List;

public record CardFileContent(Header header, List<Row> rows, Footer footer) { }
