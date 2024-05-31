package com.lotus.flatmate.message.mapper;

import com.lotus.flatmate.message.dto.AttachmentDto;
import com.lotus.flatmate.message.entity.Attachment;

public interface AttachmentMapper {

	AttachmentDto mapToDto(Attachment savedAttachment);

}
