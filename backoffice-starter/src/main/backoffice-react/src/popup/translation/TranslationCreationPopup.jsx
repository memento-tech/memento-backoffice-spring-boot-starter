import { useEffect, useState } from "react";
import {
  getAvailableLanguages,
  saveTranslations,
} from "../../adapters/entityAdapter";
import BasicInputField from "../../components/BasicInputField";
import { CircularProgress } from "../../components/CircularProgress";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import PopupFieldsContainer from "../../components/PopupFieldsContainer";
import PopupModal from "../PopupModal";
import PopupButtons from "../components/PopupButtons";
import { PopupDataContainer } from "../components/PopupDataContainer";
import PopupTitle from "../components/PopupTitle";

const TranslationCreationPopup = ({ zIndex, onClose, onSaveSuccess }) => {
  const [errorMessage, setErrorMessage] = useState("");
  const [availableLanguages, setAvailableLanguages] = useState([]);
  const [translationCode, setTranslationCode] = useState("");
  const [translations, setTranslations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAvailableLanguages().then((res) => {
      setErrorMessage(res.exceptionMessage);
      setAvailableLanguages(res.availableLanguages);
      setLoading(false);
    });
  }, []);

  const saveTranslation = async () => {
    var response = await saveTranslations(
      {
        translationCode: translationCode,
        translations: translations,
      },
      false
    );

    setErrorMessage("");

    if (response.status !== 200) {
      setErrorMessage(response.exceptionMessage);
    } else {
      onSaveSuccess();
      onClose();
    }
  };

  const setOrUpdateTranslation = (translationValue, langIsoCode) => {
    var newTranslations = translations;

    var existingTranslation = newTranslations.find(
      (translation) => translation.langIsoCode === langIsoCode
    );

    if (existingTranslation) {
      existingTranslation.translation = translationValue;
    } else {
      newTranslations = translations;

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

  const getFields = () => {
    return (
      <PopupFieldsContainer
        style={{ gridTemplateColumns: "1fr", width: "auto" }}
      >
        <BasicInputField
          placeholder={"Translation Code"}
          value={translationCode}
          onChange={(newValue) => setTranslationCode(newValue)}
          updatable={true}
          addMargin={false}
        />
        {availableLanguages.map((language, index) => {
          return (
            <BasicInputField
              key={index}
              placeholder={"Lang: [" + language.languageCode + "]"}
              value={getTranslationForLang(language.languageCode)}
              passwordType={false}
              onChange={(newValue) =>
                setOrUpdateTranslation(newValue, language.languageCode)
              }
              updatable={true}
              addMargin={false}
            />
          );
        })}
      </PopupFieldsContainer>
    );
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <PopupTitle entityName={"Translation"} isCreation={true} />
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      {loading && <CircularProgress />}
      {!loading && availableLanguages.length > 0 && (
        <PopupDataContainer>{getFields()}</PopupDataContainer>
      )}
      <PopupButtons saveEntity={saveTranslation} onClose={onClose} />
    </PopupModal>
  );
};

export default TranslationCreationPopup;
