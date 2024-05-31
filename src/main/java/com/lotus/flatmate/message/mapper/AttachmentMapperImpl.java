package com.lotus.flatmate.message.mapper;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.message.dto.AttachmentDto;
import com.lotus.flatmate.message.entity.Attachment;

@Component
public class AttachmentMapperImpl implements AttachmentMapper{

	@Override
	public AttachmentDto mapToDto(Attachment savedAttachment) {
		if (savedAttachment == null) {
			return null;
		}
		AttachmentDto attachmentDto = new AttachmentDto();
		attachmentDto.setId(savedAttachment.getId());
		attachmentDto.setFileName(savedAttachment.getFileName());
		attachmentDto.setFileType(savedAttachment.getFileType());
		attachmentDto.setFileBytes(savedAttachment.getFileBytes());
		attachmentDto.setCreatedAt(savedAttachment.getCreatedAt());
		attachmentDto.setUpdatedAt(savedAttachment.getUpdatedAt());
		return attachmentDto;
	}
	
}
