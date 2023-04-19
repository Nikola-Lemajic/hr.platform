import "./App.css";
import AddJobCandidate from "./components/AddJobCandidate";
import AddSkill from "./components/AddSkill";
import EditJobCandidate from "./components/EditJobCandidate";
import Home from "./components/Home";
import NavBar from "./components/NavBar";
import { Routes, Route } from "react-router-dom";

const App = () => {
  return (
    <>
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />}></Route>
        <Route path="/job" element={<AddJobCandidate />}></Route>
        <Route path="/skill" element={<AddSkill />}></Route>
        <Route path="/edit" element={<EditJobCandidate />}></Route>
      </Routes>
    </>
  );
};

export default App;
