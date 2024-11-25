import { useEffect, useState } from "react";
import styled from "styled-components";
import {
  getAvailableLanguages,
  getEntityData,
} from "../../adapters/entityAdapter";
import { CircularProgress } from "../../components/CircularProgress";
import PopupErrorMessage from "../../components/PopupErrorMessage";
import SubmitButton from "../../components/SubmitButton";
import PopupModal from "../PopupModal";
import CreationPopup from "../CreationPopup";

const TranslationSelectionPopup = ({ zIndex, onClose, onSelect }) => {
  const openCreationPopup = CreationPopup();

  const entityName = "Translation";
  const [selectedTranslation, setSelectedTranslation] = useState(null);

  const [availableLanguages, setAvailableLanguages] = useState([]);
  const [values, setValues] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");
  const [loadingLanguages, setLoadingLanguges] = useState(true);
  const [loadingData, setLoadingData] = useState(true);
  const [sortAsc, setSortAsc] = useState(true);

  const getTranslations = () => {
    getAvailableLanguages().then((res) => {
      setErrorMessage(res.exceptionMessage);
      setAvailableLanguages(res.availableLanguages);
      setLoadingLanguges(false);
    });

    getEntityData(entityName, 20, pageNumber).then((result) => {
      const responseData = result.entityData;
      setValues(responseData);
      setErrorMessage(result.exceptionMessage);
      setLoadingData(false);
    });
  };

  useEffect(() => {
    getTranslations();
  }, []);

  const closeModal = () => {
    setErrorMessage("");
    onClose();
  };

  const sortEntityData = (fieldName) => {
    let sorted = [];
    if (sortAsc) {
      sorted = [...values].sort(function (a, b) {
        if (a.data[fieldName] < b.data[fieldName]) {
          return -1;
        }
        if (a.data[fieldName] > b.data[fieldName]) {
          return 1;
        }
        return 0;
      });
    } else {
      sorted = [...values].sort(function (a, b) {
        if (a.data[fieldName] > b.data[fieldName]) {
          return -1;
        }
        if (a.data[fieldName] < b.data[fieldName]) {
          return 1;
        }
        return 0;
      });
    }

    setValues(sorted);
    setSortAsc(!sortAsc);
  };

  const getTranslationForLangIsoCode = (translationData, langIsoCode) => {
    var existingValue = translationData.translationWrappers.find(
      (wrapper) => wrapper.language.langIsoCode === langIsoCode
    );

    if (!existingValue) {
      return "";
    } else {
      return existingValue.translation;
    }
  };

  const isSelected = (recordData) => {
    return selectedTranslation?.data.code === recordData.data.code;
  };

  const getTranslationValues = () => {
    return values.map((recordData, index) => {
      return (
        <Row
          key={index}
          onClick={() => setSelectedTranslation(recordData)}
          // @ts-ignore
          $selected={isSelected(recordData)}
        >
          <td key={index}>{recordData.data.code}</td>
          {availableLanguages &&
            availableLanguages.map((lang, index) => {
              return (
                <td key={index}>
                  {getTranslationForLangIsoCode(
                    recordData.translationData,
                    lang.languageCode
                  )}
                </td>
              );
            })}
        </Row>
      );
    });
  };

  const getEntityFields = () => {
    return (
      <>
        <th className="sort" onClick={() => sortEntityData("code")}>
          Code
        </th>
        {availableLanguages &&
          availableLanguages.map((lang, index) => (
            <th key={index}>Lang [{lang.languageCode}]</th>
          ))}
      </>
    );
  };

  const onSelectInternal = () => {
    onSelect({
      entityName: selectedTranslation.entityName,
      multivalue: false,
      valueIds: [selectedTranslation.data.id],
      simpleName: selectedTranslation.data.code,
      translationData: selectedTranslation.translationData,
    });
  };

  return (
    <PopupModal isOpen={true} zIndex={zIndex} onClose={closeModal}>
      <p>Select translation</p>
      <PopupErrorMessage>{errorMessage}</PopupErrorMessage>
      <PopupMainContainer>
        {(loadingLanguages || loadingData) && <CircularProgress />}
        {!loadingLanguages && !loadingData && values.length > 0 && (
          <DataTable>
            <thead>
              <tr>{getEntityFields()}</tr>
            </thead>
            <tbody>
              {values && values.length > 0 && getTranslationValues()}
            </tbody>
          </DataTable>
        )}
        {!loadingLanguages &&
          !loadingData &&
          values.length === 0 &&
          "There are no translations"}
      </PopupMainContainer>
      <div style={{ display: "flex" }}>
        <SubmitButton
          onClick={() => {
            openCreationPopup(entityName, () => getTranslations());
          }}
        >
          Add New
        </SubmitButton>
        <SubmitButton
          onClick={() => {
            onSelectInternal();
            closeModal();
          }}
        >
          Select
        </SubmitButton>
        <SubmitButton onClick={() => onClose()}>Cancel</SubmitButton>
      </div>
    </PopupModal>
  );
};

export default TranslationSelectionPopup;

const PopupMainContainer = styled.div`
  display: flex;
  max-width: 800px;
  overflow-x: scroll;
  overflow-y: scroll;
`;

const Row = styled.tr`
  cursor: pointer;

  background-color: ${(props) => (props.$selected ? "#1db53977" : "unset")};

  &:hover {
    text-shadow: ${(props) => (props.$selected ? "" : "0px 0px 1px black")};
  }
`;

const DataTable = styled.table`
  border-collapse: collapse;
  margin: 0 5px 1rem 5px;
  font-size: 0.9em;
  font-family: sans-serif;
  overflow-x: scroll;
  overflow-y: scroll;
  background-color: #ffffff;

  & thead {
    background-color: ${(props) => props.theme.colors.primary};
    color: #ffffff;
    border: 1px solid ${(props) => props.theme.colors.primary};
    & th.sort {
      box-sizing: border-box;
      text-align: center;
      cursor: pointer;
      &:hover {
        background-color: #ffffff;
        color: ${(props) => props.theme.colors.primary};
      }
    }
  }

  & th {
    font-size: 1rem;
    font-weight: 200;
  }

  & th,
  td {
    padding: 5px 15px;
    border: 1px solid #0e7979;
  }

  & td {
    max-width: 200px;
    overflow-y: scroll;
    overflow-x: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    text-align: start;
  }

  & tbody tr {
    border-bottom: 1px solid #0e7979;
  }
`;
