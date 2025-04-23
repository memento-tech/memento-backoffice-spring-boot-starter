import { useState } from "react";
import { saveEntityData } from "../../adapters/entityAdapter";
import BasicInputField from "../../components/BasicInputField";
import NonBasicFieldButton from "../../components/NonBasicFieldButton";
import PopupModal from "../PopupModal";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import { PopupDataContainer } from "../components/PopupDataContainer";
import PopupButtons from "../components/PopupButtons";
import PopupTitle from "../components/PopupTitle";

const EntityCreationPopup = ({
  entityMetadata,
  zIndex,
  onClose,
  onSaveSuccess = () => {},
}) => {
  const [data, setData] = useState({});
  const [errorMessage, setErrorMessage] = useState("");

  const createEntity = () => {
    setErrorMessage("");
    saveEntityData(
      {
        entityName: entityMetadata.entityName,
        entityData: data,
        isCreation: true,
      },
      true
    ).then((result) => {
      if (result.status === 200) {
        onSaveSuccess();
        closeModal();
      } else {
        setErrorMessage(result.exceptionMessage);
      }
    });
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getFields = () => {
    return (
      <PopupFieldsContainer>
        {entityMetadata.creationSettingsMetadata.allowCreation &&
          entityMetadata.creationSettingsMetadata.creationFields.map(
            (field, index) => {
              if (field.basic) {
                return (
                  <BasicInputField
                    style={field.required ? { borderColor: "red" } : {}}
                    key={index}
                    placeholder={field.name}
                    value={""}
                    passwordType={"password" === field.id}
                    onChange={(newValue) => {
                      setData((prevData) => ({
                        ...prevData,
                        [field.id]: newValue,
                      }));
                    }}
                    updatable={true}
                  />
                );
              }
              return (
                <NonBasicFieldButton
                  key={index}
                  field={field}
                  valueData={data[field.id]}
                  onSelect={(selectedData) => {
                    setData((prevData) => ({
                      ...prevData,
                      [field.id]: selectedData,
                    }));
                  }}
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
                />
              );
            }
          )}
      </PopupFieldsContainer>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <PopupTitle entityName={entityMetadata.entityName} isCreation={true} />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      <PopupDataContainer>{getFields()}</PopupDataContainer>
      <PopupButtons createEntity={createEntity} onClose={onClose} />
    </PopupModal>
  );
};

export default EntityCreationPopup;
