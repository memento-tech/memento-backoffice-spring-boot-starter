import styled from "styled-components";
import { useEffect, useState } from "react";
import { getAvailableLanguages, saveTranslations } from "../../adapters/entityAdapter";
import BasicInputField from "../../components/BasicInputField";
import { CircularProgress } from "../../components/CircularProgress";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import SubmitButton from "../../components/SubmitButton";
import PopupModal from "../PopupModal";

const TranslationPopup = ({
  translationData,
  zIndex,
  onClose,
  onSaveSuccess,
}) => {
  const [errorMessage, setErrorMessage] = useState("");
  const [translations, setTranslations] = useState([]);
  const [availableLanguages, setAvailableLanguages] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!translationData) {
      setErrorMessage("Somthing went wrong!");
    } else {
      var _translations = translationData.translationWrappers.map(
        (translationWrapper) => {
          return {
            langIsoCode: translationWrapper.language.langIsoCode,
            translation: translationWrapper.translation,
          };
        }
      );

      setTranslations(_translations);
      getAvailableLanguages().then((res) => {
        setErrorMessage(res.exceptionMessage);
        setAvailableLanguages(res.availableLanguages);
        setLoading(false);
      });
    }
  }, []);

  const saveTranslation = async () => {
    var response = await saveTranslations(
      {
        translationCode: translationData.code,
        translations: translations,
      },
      true
    );

    setErrorMessage("");

    if (response.status !== 200) {
      setErrorMessage(response.exceptionMessage);
    } else {
      onSaveSuccess();
      closeModal();
    }
  };

  const getTranslationForLang = (langIsoCode) => {
    var existingTranslation = translations.find(
      (translation) => translation.langIsoCode === langIsoCode
    );

    if (existingTranslation) {
      return existingTranslation.translation;
    } else {
      return "";
    }
  };

  const setOrUpdateTranslationInternally = (translationValue, langIsoCode) => {
    var newTranslations = translations;

    var existingTranslation = newTranslations.find(
      (translation) => translation.langIsoCode === langIsoCode
    );

    if (existingTranslation) {
      existingTranslation.translation = translationValue;
    } else {
      var newTranslations = translations;

      newTranslations.push({
        langIsoCode: langIsoCode,
        translation: translationValue,
      });
    }

    setTranslations(newTranslations);
  };

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const getFields = () => {
    return (
      <PopupContainer>
        <PopupFieldsContainer
          style={{ gridTemplateColumns: "1fr", width: "auto" }}
        >
          <BasicInputField
            placeholder={"Translation Code"}
            value={translationData.code}
            passwordType={false}
            onChange={() => {}}
            updatable={false}
            addMargin={false}
          />
          {availableLanguages.map((language, index) => (
            <BasicInputField
              key={index}
              placeholder={"Lang: [" + language.languageCode + "]"}
              value={getTranslationForLang(language.languageCode)}
              passwordType={false}
              onChange={(newValue) =>
                setOrUpdateTranslationInternally(
                  newValue,
                  language.languageCode
                )
              }
              updatable={true}
              addMargin={false}
            />
          ))}
        </PopupFieldsContainer>
      </PopupContainer>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <>
        <p>Update Translation</p>
        <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
        {loading && <CircularProgress />}
        {!loading && !errorMessage && (
          <PopupMainContainer>{getFields()}</PopupMainContainer>
        )}
        <div style={{ display: "flex" }}>
          {!loading && !errorMessage && (
            <SubmitButton onClick={() => saveTranslation()}>Save</SubmitButton>
          )}
          <SubmitButton onClick={() => closeModal()}>Cancel</SubmitButton>
        </div>
      </>
    </PopupModal>
  );
};

export default TranslationPopup;

const PopupContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const PopupMainContainer = styled.div`
  display: flex;
  flex-direction: column;
`;
