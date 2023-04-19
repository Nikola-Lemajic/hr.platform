import axios from "axios";

var Axios = axios.create({
  baseURL: "http://localhost:8080/api",
});

export default Axios;
