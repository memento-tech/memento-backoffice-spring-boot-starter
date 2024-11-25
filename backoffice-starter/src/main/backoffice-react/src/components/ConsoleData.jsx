import styled from "styled-components";
import DataNav from "./DataNav";
import { useEffect, useState } from "react";
import NonBasicButton from "./NonBasicButton";
import { useDispatch, useSelector } from "react-redux";
import { fetchEntityData } from "../redux/reducers/entityDataSlice";
import { deleteEntityData } from "../adapters/entityAdapter";
import { useNavigate } from "react-router-dom";
import TrashIcon from "./icons/TrashIcon";
import DataPopup from "../popup/DataPopup";

const getPageNumberButtons = (numberOfPages) => {
  var pageNumbers = [];

  for (let i = 0; i < numberOfPages; i++) {
    pageNumbers.push(i);
  }

  return pageNumbers;
};

const ConsoleData = () => {
  const entityMetadataState = useSelector(
    (state) => state.entityMetadatasState
  );
  const entityDataState = useSelector((state) => state.entityDataState);

  const [selectedEntity, setSelectedEntity] = useState(null);

  useEffect(() => {
    setSelectedEntity(entityMetadataState.selectedEntityMetadata);
  }, [entityDataState, entityMetadataState]);

  const dispatch = useDispatch();
  const openDataPopup = DataPopup();

  const navigate = useNavigate();

  const [dataOverflowHidden, setDataOverflowHidden] = useState(false);
  const [data, setData] = useState([]);
  const [sortAsc, setSortAsc] = useState(true);
  const [pageSize, setPageSize] = useState(10);
  const [pageNumber, setPageNumber] = useState(0);

  const [numberOfPages, setNumberOfPages] = useState(0);

  useEffect(() => {
    if (selectedEntity) {
      setNumberOfPages(Math.ceil(selectedEntity.numOfRecords / pageSize));
    }
  }, [pageSize, selectedEntity]);

  const getOrRefreshEntityData = () => {
    dispatch(
      fetchEntityData({
        entityName: selectedEntity.entityName,
        pageSize: pageSize,
        pageNumber: pageNumber,
      })
    );
  };

  useEffect(() => {
    if (selectedEntity) {
      getOrRefreshEntityData();
    }
  }, [selectedEntity, pageSize, pageNumber, dispatch]);

  useEffect(() => {
    setData([...entityDataState.entityData]);
  }, [entityDataState.entityData]);

  const sortEntityData = (fieldName) => {
    let sorted = [];
    if (sortAsc) {
      sorted = [...data].sort(function (a, b) {
        if (a.data[fieldName] < b.data[fieldName]) {
          return -1;
        }
        if (a.data[fieldName] > b.data[fieldName]) {
          return 1;
        }
        return 0;
      });
    } else {
      sorted = [...data].sort(function (a, b) {
        if (a.data[fieldName] > b.data[fieldName]) {
          return -1;
        }
        if (a.data[fieldName] < b.data[fieldName]) {
          return 1;
        }
        return 0;
      });
    }

    setData(sorted);
    setSortAsc(!sortAsc);
  };

  const getEntityFields = () => {
    return (
      <>
        {selectedEntity?.entityFields.map((field, index) => (
          <th
            className="sort"
            key={index}
            onClick={() => sortEntityData(field.id)}
          >
            {field.name}
          </th>
        ))}
      </>
    );
  };

  const getUpdateNewEntityPopup = (recordData) => {
    setDataOverflowHidden(true);
    openDataPopup(
      selectedEntity.entityName,
      false,
      recordData,
      getOrRefreshEntityData,
      undefined
    );
  };

  const deleteEntity = async (recordData) => {
    const response = await deleteEntityData(
      recordData.entityName,
      recordData.data?.id
    );
    if (response.status !== 200) {
      console.log(response.errorMessage);
    } else {
      navigate(0);
    }
  };

  const getEntityValues = () => {
    return (
      selectedEntity.entityName === data[0].entityName &&
      data.map((recordData, index) => {
        return (
          <Row key={index} onClick={() => getUpdateNewEntityPopup(recordData)}>
            <td style={{ width: "5px" }}>
              <TrashIcon
                onClick={(e) => {
                  e.stopPropagation();
                  deleteEntity(recordData);
                }}
              />
            </td>
            <td style={{ textAlign: "center", width: "5px" }}>
              {index + 1 + pageNumber * pageSize}
            </td>
            {selectedEntity.entityFields.map((field, index) => {
              if (field.basic) {
                return <td key={index}>{recordData.data[field.id]}</td>;
              }
              if (
                !recordData.data[field.id] &&
                recordData.data[field.id] === ""
              ) {
                return <td key={index}></td>;
              }

              return (
                <td key={index}>
                  <NonBasicButton
                    valueData={recordData.data[field.id]}
                    onClick={() => {}}
                  />
                </td>
              );
            })}
          </Row>
        );
      })
    );
  };

  return (
    <Data
      id="console-data"
      className={dataOverflowHidden ? "overflowHidden" : ""}
    >
      <DataNav />
      <SpacerLine />
      {selectedEntity && (
        <>
          <DataTable>
            <thead>
              <tr>
                <th></th>
                <th>No</th>
                {getEntityFields()}
              </tr>
            </thead>
            <tbody>{data && data.length > 0 && getEntityValues()}</tbody>
          </DataTable>
          <FooterDiv>
            {selectedEntity.numOfRecords > 0 && (
              <PagingDiv>
                <RowsPerPageDiv>
                  Rows per page:
                  <select
                    onChange={(e) => {
                      let size = e.target.options[e.target.selectedIndex].value;
                      setPageSize(parseInt(size));
                    }}
                  >
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="50">50</option>
                    <option value="-1">All</option>
                  </select>
                </RowsPerPageDiv>
                {getPageNumberButtons(numberOfPages).map((page, index) => (
                  <PageNumberButton
                    key={index}
                    className={page === pageNumber ? "active" : ""}
                    disabled={page === pageNumber}
                    onClick={() => {
                      setPageNumber(page);
                    }}
                  >
                    {page}
                  </PageNumberButton>
                ))}
              </PagingDiv>
            )}
            <DataCount>
              Total number of records: {selectedEntity.numOfRecords}
            </DataCount>
          </FooterDiv>
        </>
      )}
    </Data>
  );
};

export default ConsoleData;

const Data = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  background-color: #f0f0f0;

  &.overflowHidden {
    overflow: hidden;
  }
`;

const SpacerLine = styled.hr`
  margin: 1rem 5px;
  border-color: ${(props) => props.theme.colors.primary};
`;

const DataTable = styled.table`
  border-collapse: collapse;
  margin: 0 5px 0 5px;
  font-size: 0.9em;
  font-family: sans-serif;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
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

  & th,
  td {
    padding: 5px 15px;
    border-right: 1px solid #0e7979;
    white-space: nowrap;
    max-width: 300px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  & tbody tr {
    border-bottom: 1px solid #0e7979;
  }
`;

const Row = styled.tr`
  cursor: pointer;

  &:hover {
    text-shadow: 0px 0px 1px black;
  }
`;

const FooterDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin: 10px 10px;
`;

const DataCount = styled.div`
  align-self: flex-start;
  margin: auto 0 0 0;
`;

const PagingDiv = styled.div`
  display: flex;
  align-items: center;
`;

const RowsPerPageDiv = styled.div`
  display: flex;
`;

const PageNumberButton = styled.button`
  font-size: 1.3rem;
  width: 30px;
  height: 30px;
  border-radius: 15px;
  border: 1px solid #676868a2;
  margin: 10px 3px;

  &.active {
    cursor: unset;
    background-color: #84fcfc9b;
    border-color: #ffffff;
  }
`;
