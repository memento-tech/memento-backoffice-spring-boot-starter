import styled from "styled-components";

const PopupTitle = ({
  entityName,
  isCreation = false,
  isSelection = false,
  isUpdate = false,
}) => {
  var title = "Please set title!";

  if (isCreation) {
    title = "Create new " + entityName;
  } else if (isSelection) {
    title = "Select " + entityName;
  } else if (isUpdate) {
    title = "Update " + entityName;
  }

  return <Title>{title}</Title>;
};

export default PopupTitle;

const Title = styled.h3`
  font-size: 18px;
`;
