import styled from "styled-components";
import SubmitButton from "../../components/SubmitButton";

const PopupButtons = ({
  selectEntity = null,
  createEntity = null,
  saveEntity = null,
  onClose = null,
}) => {
  return (
    <ButtonsContainer style={{ display: "flex" }}>
      {selectEntity && (
        <SubmitButton onClick={selectEntity}>Select</SubmitButton>
      )}
      {createEntity && (
        <SubmitButton onClick={createEntity}>Create</SubmitButton>
      )}
      {saveEntity && <SubmitButton onClick={saveEntity}>Save</SubmitButton>}
      {onClose && <SubmitButton onClick={onClose}>Cancel</SubmitButton>}
    </ButtonsContainer>
  );
};

export default PopupButtons;

const ButtonsContainer = styled.div`
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: center;
`;
