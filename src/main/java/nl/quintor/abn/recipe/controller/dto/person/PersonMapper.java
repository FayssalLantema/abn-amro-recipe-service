package nl.quintor.abn.recipe.controller.dto.person;

import nl.quintor.abn.recipe.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto toPersonDto(Person person);

}
