package nl.quintor.abn.recipe.controller.dto.instruction;

import nl.quintor.abn.recipe.model.Instruction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstructionMapper {

    InstructionMapper INSTANCE = Mappers.getMapper(InstructionMapper.class);

    InstructionDto toInstructionDto(Instruction instruction);

}
