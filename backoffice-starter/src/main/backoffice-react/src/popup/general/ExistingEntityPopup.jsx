import styled from "styled-components";
import { useEffect, useState } from "react";
import {
  getEntityDataForId,
  saveEntityData,
} from "../../adapters/entityAdapter";
import BasicInputField from "../../components/BasicInputField";
import NonBasicFieldButton from "../../components/NonBasicFieldButton";
import { CircularProgress } from "../../components/CircularProgress";
import PopupModal from "../PopupModal";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import WidgetButton from "../../widget/WidgetButton";
import PopupTitle from "../components/PopupTitle";
import PopupButtons from "../components/PopupButtons";
import { PopupDataContainer } from "../components/PopupDataContainer";

const ExistingEntityPopup = ({
  id,
  entityMetadata,
  zIndex,
  onClose,
  onSaveSuccess = () => {},
}) => {
  const [data, setData] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");

  const getEntityData = () => {
    getEntityDataForId(entityMetadata.entityName, id).then((result) => {
      setData(result.entityData.data);
      setErrorMessage(result.exceptionMessage);
    });
  };

  useEffect(() => {
    getEntityData();
  }, []);

  const saveEntity = async () => {
    var response = await saveEntityData({
      entityName: entityMetadata.entityName,
      entityData: data,
    });
    setErrorMessage("");

    if (response.status !== 200) {
      setErrorMessage(response.exceptionMessage);
    } else {
      setData(data);
      onSaveSuccess();
      closeModal();
    }
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getFields = () => {
    return (
      <PopupFieldsContainer>
        {entityMetadata.entityFields.map((field, index) => {
          if (field.basic) {
            return (
              <BasicInputField
                style={field.required ? { borderColor: "red" } : {}}
                key={index}
                placeholder={field.name}
                value={data ? data[field.id] : ""}
                passwordType={"password" === field.id}
                onChange={(newValue) => {
                  setData((prevData) => ({
                    ...prevData,
                    [field.id]: newValue,
                  }));
                }}
                updatable={field.updatable}
              />
            );
          }
          return (
            <NonBasicFieldButton
              key={index}
              field={field}
              valueData={data ? data[field.id] : ""}
              onSaveSuccess={getEntityData}
              onRemove={() => {
                setData((prevData) => ({
                  ...prevData,
                  [field.id]: null,
                }));
              }}
              onMultivalueSelect={(newValue) => {
                setData((prevData) => ({
                  ...prevData,
                  [field.id]: newValue,
                }));
              }}
              onSelect={(selectedData) => {
                setData((prevData) => ({
                  ...prevData,
                  [field.id]: selectedData,
                }));
              }}
            />
          );
        })}
      </PopupFieldsContainer>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <PopupTitle entityName={entityMetadata.entityName} isUpdate={true} />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      {!data && <CircularProgress />}
      {data && (
        <WidgetContainer>
          {entityMetadata.widgets
            ?.filter((widget) => widget.recordLevel)
            .map((widget, index) => {
              return (
                <WidgetButton
                  key={index}
                  widget={widget}
                  entityName={entityMetadata.entityName}
                  recordId={data.id}
                />
              );
            })}
        </WidgetContainer>
      )}
      {data && <PopupDataContainer>{getFields()}</PopupDataContainer>}
      <PopupButtons saveEntity={saveEntity} onClose={onClose} />
    </PopupModal>
  );
};

export default ExistingEntityPopup;

const WidgetContainer = styled.div`
  width: 100%;
  margin: auto;
  display: flex;
  justify-content: end;
  align-items: center;
`;
