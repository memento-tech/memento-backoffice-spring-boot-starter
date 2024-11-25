import API from "../config/api";

export async function refreshEntityMetadata() {
  return API.get("/api/backoffice/metadata/refresh")
    .then((res) => {
      return {
        entityMetadata: res.data,
        exceptionMessage: "",
      };
    })
    .catch((res) => {
      return {
        entityMetadatas: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function getEntityOverview() {
  return API.get("/api/backoffice/metadata/all")
    .then((res) => {
      return {
        entityMetadata: res.data,
        exceptionMessage: "",
      };
    })
    .catch((res) => {
      return {
        entityMetadatas: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function getEntityData(entityName, pageSize, pageNumber) {
  return API.get(
    "/api/backoffice/entity/all?entityName=" +
      entityName +
      "&pageSize=" +
      pageSize +
      "&pageNumber=" +
      pageNumber
  )
    .then((res) => {
      return {
        entityData: res.data,
        exceptionMessage: res.data.exceptionMessage,
      };
    })
    .catch((res) => {
      return {
        entityData: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function getEntityDataForId(entityName, entityId) {
  return API.get(
    "/api/backoffice/entity?entityName=" + entityName + "&recordId=" + entityId
  )
    .then((res) => {
      return {
        entityData: res.data,
        exceptionMessage: "",
      };
    })
    .catch((res) => {
      return {
        entityData: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function getEntitiesDataForIds(entityName, entityIds) {
  return API.get(
    "/api/backoffice/entity/list?entityName=" +
      entityName +
      "&recordIds=" +
      entityIds
  )
    .then((res) => {
      return {
        entityData: res.data,
        exceptionMessage: "",
      };
    })
    .catch((res) => {
      return {
        entityData: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function saveEntityData(saveDataBundle, isNew = false) {
  let exceptionMessage = "";
  let status = -1;

  await API.post("/api/backoffice/entity/save?isNew=" + isNew, saveDataBundle)
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}

export async function deleteEntityData(entityName, recordId) {
  let exceptionMessage = "";
  let status = -1;

  var bodyData = { entityName: entityName, recordId: recordId };

  await API.post("/api/backoffice/entity/delete", bodyData)
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}

export async function getAvailableLanguages() {
  return API.get("/api/backoffice/translation/lang")
    .then((res) => {
      return {
        availableLanguages: res.data,
        exceptionMessage: res.data?.exceptionMessage,
      };
    })
    .catch((res) => {
      return {
        availableLanguages: [],
        exceptionMessage: res.data?.exceptionMessage,
      };
    });
}

export async function saveTranslations(translationsData, isUpdate) {
  let exceptionMessage = "";
  let status = -1;

  await API.post(
    "/api/backoffice/translation?update=" + isUpdate,
    translationsData
  )
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}

export async function deleteTranslations(translationCode) {
  let exceptionMessage = "";
  let status = -1;

  await API.delete(
    "/api/backoffice/translation?translationCode=",
    translationCode
  )
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}

export async function saveMedia(mediaData) {
  let exceptionMessage = "";
  let status = -1;

  await API.post("/api/backoffice/media/add", mediaData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}

export async function updateMedia(mediaData) {
  let exceptionMessage = "";
  let status = -1;

  await API.post("/api/backoffice/media/update", mediaData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
    .then((res) => {
      status = res.status;
    })
    .catch((err) => {
      exceptionMessage = err.data?.exceptionMessage;
      status = err?.status;
    });

  return { exceptionMessage, status };
}
