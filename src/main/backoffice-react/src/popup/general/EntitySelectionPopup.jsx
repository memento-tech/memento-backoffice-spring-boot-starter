import { useEffect, useState } from "react";
import { getEntityData } from "../../adapters/entityAdapter";
import PopupModal from "../PopupModal";
import styled from "styled-components";
import { CircularProgress } from "../../components/CircularProgress";
import NonBasicFieldButton from "../../components/NonBasicFieldButton";
import BasicInputField from "../../components/BasicInputField";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import PopupButtons from "../components/PopupButtons";
import { PopupDataContainer } from "../components/PopupDataContainer";
import PopupTitle from "../components/PopupTitle";
import MultivaluePopupButtonsContainer from "../components/MultivaluePopupButtonsContainer";

const EntitySelectionPopup = ({
  entityMetadata,
  selectedIds,
  multivalue,
  onSelect,
  onClose,
  zIndex,
}) => {
  const [values, setValues] = useState([]);
  const [visibleData, setVisibleData] = useState(null);
  const [selectedData, setSelectedData] = useState({
    entityName: entityMetadata.entityName,
    multivalue: multivalue,
    simpleName: entityMetadata.entityName + "(0)",
    valueIds: selectedIds,
  });
  const [pageNumber, setPageNumber] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(true);

  const setSelectionData = (dataSelected) => {
    var ids = selectedData.valueIds;
    const selectedId = dataSelected["id"];

    if (multivalue) {
      if (ids.some((id) => id === selectedId)) {
        ids = ids.filter((id) => id !== selectedId);
      } else {
        ids.push(selectedId);
      }
    } else {
      ids = [selectedId];
    }

    const firstId = ids.length === 0 ? ids[0] : "";
    const simpleNameValue = multivalue ? "..." + ids.length : firstId;

    setSelectedData((prevData) => ({
      ...prevData,
      valueIds: ids,
      simpleName: entityMetadata.entityName + "(" + simpleNameValue + ")",
    }));
  };

  useEffect(() => {
    getEntityData(entityMetadata.entityName, 20, pageNumber).then((result) => {
      const responseData = result.entityData;
      setValues(responseData);
      if (responseData.length > 0) {
        setVisibleData(responseData[0].data);
        if (!multivalue) {
          setSelectionData(responseData[0].data);
        }
      }
      setErrorMessage(result.exceptionMessage);
      setLoading(false);
    });
  }, []);

  const showDataWithId = (id) => {
    setVisibleData(getDataForId(id));
  };

  const getDataForId = (id) => {
    return values.find((entityData) => entityData.data["id"] === id).data;
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getVisibleEntityFieldValues = () => {
    return entityMetadata.entityFields.map((field, index) => {
      if (field.basic) {
        return (
          <BasicInputField
            key={index}
            placeholder={field.name}
            value={visibleData[field.id]}
            passwordType={"password" === field.id}
            onChange={() => {}}
            updatable={false}
          />
        );
      } else {
        return (
          <NonBasicFieldButton
            key={index}
            disabled={true}
            field={field}
            valueData={visibleData[field.id]}
            onSaveSuccess={getEntityData}
            showRemoveIcon={false}
            onRemove={() => {}}
            useRequired={false}
          />
        );
      }
    });
  };

  const getButtonLabel = (dataValue) => {
    var label = getDataForId(dataValue["id"])[
      entityMetadata.fieldForShowInList
    ];

    if (
      typeof label === "object" &&
      label !== null &&
      label.entityName === "Translation"
    ) {
      label = label.simpleName;
    }

    return label;
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <PopupTitle entityName={entityMetadata.entityName} isSelection={true} />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      <PopupDataContainer>
        {loading && <CircularProgress />}
        {!loading && values.length > 0 && visibleData && (
          <>
            <DataContainer>
              <PopupFieldsContainer>
                {getVisibleEntityFieldValues()}
              </PopupFieldsContainer>
            </DataContainer>
            <MultivaluePopupButtonsContainer
              values={values}
              multivalue={multivalue}
              selectedData={selectedData}
              onChecked={setSelectionData}
              onNonMultivalueSelection={setSelectionData}
              onDataShow={showDataWithId}
              getButtonLabel={getButtonLabel}
            />
          </>
        )}
        {!loading &&
          values.length === 0 &&
          !visibleData &&
          "No entity records found for type " + entityMetadata.entityName}
      </PopupDataContainer>
      <PopupButtons
        selectEntity={() => {
          onSelect(selectedData);
          closeModal();
        }}
        onClose={onClose}
      />
    </PopupModal>
  );
};

export default EntitySelectionPopup;

const DataContainer = styled.div`
  border-right: 1px solid black;
`;
