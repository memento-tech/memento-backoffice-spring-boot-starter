import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getEntityData } from "../../adapters/entityAdapter";

const initialState = {
  entityData: [],
  loading: true,
  errorMessage: "",
};

export const fetchEntityData = createAsyncThunk(
  "entity/fetchEntityData",
  async (args) => {
    var response = await getEntityData(
      args.entityName,
      args.pageSize,
      args.pageNumber
    );

    return {
      entityData: response.entityData,
      errorMessage: response.exceptionMessage,
    };
  }
);

export const entityMetadataSlice = createSlice({
  name: "entityMetadatas",
  initialState,
  extraReducers: (builder) => {
    builder.addCase(fetchEntityData.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(fetchEntityData.fulfilled, (state, action) => {
      state.loading = false;
      state.entityData = action.payload.entityData;
      state.errorMessage = action.payload.errorMessage;
    });
    builder.addCase(fetchEntityData.rejected, (state, action) => {
      state.loading = false;
      state.entityData = [];
      state.errorMessage = "Fetch is rejected";
    });
  },
  reducers: {
    updateEntityData: (state, action) => {
      state.entityData = action.payload.entityData;
    },
  },
});

export const { updateEntityData } = entityMetadataSlice.actions;

export default entityMetadataSlice.reducer;
