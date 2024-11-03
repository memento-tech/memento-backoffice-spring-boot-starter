import styled from "styled-components";
import MultivaluePopupButton from "./MultivaluePopupButton";

const MultivaluePopupButtonsContainer = ({
  values,
  multivalue,
  selectedData,
  onChecked,
  onNonMultivalueSelection,
  onDataShow,
  getButtonLabel,
}) => {
  return (
    <MultivalueContainer>
      {values
        .map((data) => data.data)
        .map((dataValue, index) => (
          <MultivaluePopupButton
            key={index}
            multivalue={multivalue}
            selectedData={selectedData}
            onChecked={onChecked}
            onNonMultivalueSelection={onNonMultivalueSelection}
            onDataShow={onDataShow}
            getButtonLabel={getButtonLabel}
            dataValue={dataValue}
          />
        ))}
    </MultivalueContainer>
  );
};

export default MultivaluePopupButtonsContainer;

const MultivalueContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: start;
`;
