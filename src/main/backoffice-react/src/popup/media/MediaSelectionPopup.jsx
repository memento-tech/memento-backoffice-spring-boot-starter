import { useState, useEffect } from "react";
import styled from "styled-components";
import { getEntityData } from "../../adapters/entityAdapter";
import { CircularProgress } from "../../components/CircularProgress";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import SubmitButton from "../../components/SubmitButton";
import PopupModal from "../PopupModal";
import Checkbox from "../../components/Checkbox";
import { OutlinedButton } from "../../components/OutlinedButton";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import BasicInputField from "../../components/BasicInputField";
import UpdateEntityIcon from "../../components/icons/UpdateEntityIcon";
import PopupButtons from "../components/PopupButtons";
import { PopupDataContainer } from "../components/PopupDataContainer";
import PopupTitle from "../components/PopupTitle";
import ImagePreview from "../components/ImagePreview";
import MultivaluePopupButtonsContainer from "../components/MultivaluePopupButtonsContainer";

const MediaSelectionPopup = ({
  entityMetadata,
  selectedIds,
  multivalue,
  onSelect,
  onClose,
  zIndex,
}) => {
  const [medias, setMedias] = useState([]);
  const [visibleMedia, setVisibleMedia] = useState(null);
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

    const firstId = ids.length > 0 ? ids[0] : "";
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
      setMedias(responseData);
      if (responseData.length > 0) {
        setVisibleMedia(responseData[0].data);
        if (!multivalue) {
          setSelectionData(responseData[0].data);
        }
      }
      setErrorMessage(result.exceptionMessage);
      setLoading(false);
    });
  }, []);

  const showDataWithId = (id) => {
    setVisibleMedia(getDataForId(id));
  };

  const getDataForId = (id) => {
    return medias.find((entityData) => entityData.data["id"] === id).data;
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getVisibleMediaFieldValues = () => {
    {
      return entityMetadata.entityFields.map((field, index) => {
        if (field.basic) {
          return (
            <BasicInputField
              key={index}
              placeholder={field.name}
              value={visibleMedia[field.id]}
              passwordType={false}
              onChange={() => {}}
              updatable={false}
            />
          );
        }
      });
    }
  };

  const getButtonLabel = (dataValue) => {
    return dataValue.originalFileName;
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <PopupTitle entityName={entityMetadata.entityName} isSelection={true} />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      <PopupDataContainer>
        {loading && <CircularProgress />}
        {!loading && medias.length > 0 && visibleMedia && (
          <>
            <DataContainer>
              <PopupFieldsContainer>
                {getVisibleMediaFieldValues()}
              </PopupFieldsContainer>
              <ImagePreview mediaUrl={visibleMedia.mediaUrl} />
            </DataContainer>
            <MultivaluePopupButtonsContainer
              values={medias}
              multivalue={multivalue}
              selectedData={selectedData}
              onChecked={setSelectionData}
              onNonMultivalueSelection={setSelectionData}
              onDataShow={showDataWithId}
              getButtonLabel={getButtonLabel}
            />
          </>
        )}
        {!loading && medias.length === 0 && "There are no medias"}
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

export default MediaSelectionPopup;

const DataContainer = styled.div`
  border-right: 1px solid black;
`;
