import styled from "styled-components";
import Checkbox from "../../components/Checkbox";
import UpdateEntityIcon from "../../components/icons/UpdateEntityIcon";
import { OutlinedButton } from "../../components/OutlinedButton";
import DataPopup from "../DataPopup";

const MultivaluePopupButton = ({
  multivalue,
  selectedData,
  onChecked,
  onNonMultivalueSelection,
  onDataShow,
  getButtonLabel,
  dataValue,
  onUpdateSuccess,
}) => {
  const openDataPopup = DataPopup();

  const onUpdateEntityClick = () => {
    openDataPopup(
      selectedData.entityName,
      false,
      { data: dataValue },
      onUpdateSuccess,
      () => {}
    );
  };

  const getMultivalueButton = () => {
    return (
      <OutlinedButton
        onClick={() => {
          onDataShow(dataValue["id"]);
          if (!multivalue) {
            onNonMultivalueSelection(dataValue);
          }
        }}
        style={{
          marginTop: multivalue ? "0" : "5px",
          width: "100%",
          padding: "0 5px",
          textWrap: "nowrap",
          maxWidth: "200px",
          overflow: "hidden",
          marginRight: "5px",
        }}
      >
        {getButtonLabel(dataValue)}
      </OutlinedButton>
    );
  };

  return (
    <MultivalueButtonContainer>
      {multivalue ? (
        <Checkbox
          checked={selectedData.valueIds.some((id) => id === dataValue.id)}
          onChange={() => onChecked(dataValue)}
        >
          {getMultivalueButton()}
        </Checkbox>
      ) : (
        getMultivalueButton()
      )}
      <UpdateButton title="Update" onClick={onUpdateEntityClick}>
        <UpdateEntityIcon />
      </UpdateButton>
    </MultivalueButtonContainer>
  );
};

export default MultivaluePopupButton;

const MultivalueButtonContainer = styled.div`
  display: flex;
  margin-top: 5px;
  margin-left: 0.5rem;
  margin-right: 1rem;
`;

const UpdateButton = styled.button`
  &:hover {
    scale: 1.1;
  }
`;
