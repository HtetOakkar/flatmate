package com.lotus.flatmate.message.service;

import java.util.List;

import com.lotus.flatmate.message.dto.AttachmentDto;
import com.lotus.flatmate.message.model.request.AttachmentRequest;

public interface AttachmentService {

	AttachmentDto saveFile(AttachmentRequest request);

	AttachmentDto getFile(Long id);

	List<AttachmentDto> getAllFiles();

}
