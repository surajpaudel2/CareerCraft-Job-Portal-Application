import axios from "axios";

const apiBaseUrl = axios.create({
  baseURL: "http://localhost:8081",
});

export default apiBaseUrl;
