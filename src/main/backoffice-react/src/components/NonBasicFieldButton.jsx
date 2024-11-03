import styled from "styled-components";
import NonBasicButton from "./NonBasicButton";
import TrashIcon from "./icons/TrashIcon";
import SelectionPopup from "../popup/SelectionPopup";
import DataPopup from "../popup/DataPopup";

const trashIconStyle = {
  transform: "translateY(-2px)",
  marginLeft: "3px",
  cursor: "pointer",
};

const NonBasicFieldButton = ({
  field,
  valueData,
  disabled = false,
  onSaveSuccess = () => {},
  onMultivalueSelect = (newIds) => {},
  onSelect = (selectedData) => {},
  onRemove,
  showRemoveIcon = true,
  useRequired = true,
}) => {
  const openSelectionPopup = SelectionPopup();
  const openDataPopup = DataPopup();

  const getUpdateNewEntityPopup = () => {
    var isSelection = false;

    if (!valueData) {
      isSelection = true;
    }

    if (valueData && (!valueData.valueIds || valueData.valueIds.length <= 0)) {
      isSelection = true;
    }

    if (isSelection) {
      openSelectionPopup([], field.entityName, onSelect, field.multivalue);
    } else {
      openDataPopup(
        field.entityName,
        field.multivalue,
        valueData,
        onSaveSuccess,
        onMultivalueSelect
      );
    }
  };

  return (
    <Container>
      <FieldLabel>{field.name}</FieldLabel>
      <ButtonContainer>
        <NonBasicButton
          valueData={valueData}
          onClick={(e) => {
            e.stopPropagation();
            if (!disabled) {
              getUpdateNewEntityPopup();
            }
          }}
          style={{
            transform: "translateY(-4px)",
            borderColor: useRequired && field.required ? "red" : "#bebdbd",
          }}
          disabled={disabled}
        />
        {showRemoveIcon && (
          <TrashIcon onClick={onRemove} style={trashIconStyle} />
        )}
      </ButtonContainer>
    </Container>
  );
};

export default NonBasicFieldButton;

const Container = styled.div`
  position: relative;
  height: 3.5rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin: 0.5rem;
  z-index: 1;

  width: 300px;
`;

const FieldLabel = styled.label`
  position: absolute;
  padding-left: 0.6rem;
  transform: translateY(-25px);

  height: 1rem;
  color: #606160;
  z-index: -1;
  font-style: italic;
  font-size: 0.9rem;
`;

const ButtonContainer = styled.div`
  display: flex;
`;
