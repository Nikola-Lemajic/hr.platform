import { Form, FormControl, FormGroup, FormLabel } from "react-bootstrap";
import React, { useEffect, useState, useRef } from "react";
import "../App.css";
import Axios from "../Apis/Axios";
import { Multiselect } from "multiselect-react-dropdown"

export default function AddJobCandidate(props) {

    const [skills, setSkills] = useState([]);
    const [val, setVal] = useState([]);
    const [input, setInput] = useState({
        name: "",
        dateOfBirth: "",
        contactNumber: "",
        email: ""
    });

    const [serverResponse, setServerResponse] = useState("");
    const multiselectRef = useRef();


    const fetchSkills = () => {

        Axios.get("/skill/getAll", {
            headers: {
                Accept: "application/json",
            },
        }).then(({ data }) => {
            setSkills(data);
        });
    };

    useEffect(() => {
        fetchSkills();
    }, []);

    const createJobCandidate = async (input, val) => {

        let jobCandidateDto = {
            name: input.name,
            dateOfBirth: input.dateOfBirth,
            contactNumber: input.contactNumber,
            email: input.email,
            skills: val

        };

        await Axios.post("/jobCandidate", jobCandidateDto, {
            headers: {
                Accept: "application/json",
            },
        }).then(({ data }) => {
            setServerResponse("Successfully executed");
            setInput({
                name: "",
                dateOfBirth: "",
                contactNumber: "",
                email: ""
            });
            resetValues();
            setVal([]);
            console.log(data);
        }).catch(error => {
            setServerResponse("Error has occurred");
            console.log(error.response.data.error);
        });
    };


    function selectionChanged(selectedList, selectedItem) {
        console.log(selectedList)
        setVal(selectedList);
    }



    function handleChange(e) {
        const value = e.target.value;
        setInput({
            ...input,
            [e.target.name]: value
        });
        console.log(e.target.value);
    }


    function resetValues() {
        multiselectRef.current.resetSelectedValues();
    }




    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                <h2>
                    Add candidate
                </h2>
            </div>
            <Form className="form">
                <FormGroup >
                    <FormLabel >
                        Name:
                    </FormLabel>
                    <FormControl type="text" name="name" value={input.name} onChange={handleChange}></FormControl>
                </FormGroup>
                <FormGroup>
                    <FormLabel>
                        Date Of Birth:
                    </FormLabel>
                    <FormControl type="text" name="dateOfBirth" value={input.dateOfBirth} onChange={handleChange}></FormControl>
                </FormGroup>
                <FormGroup>
                    <FormLabel>
                        Contact Number:
                    </FormLabel>
                    <FormControl type="text" name="contactNumber" value={input.contactNumber} onChange={handleChange}></FormControl>
                </FormGroup>
                <FormGroup>
                    <FormLabel>
                        Email:
                    </FormLabel>
                    <FormControl type="text" name="email" value={input.email} onChange={handleChange}></FormControl>
                </FormGroup>
                <FormGroup>
                    <FormLabel>
                        Skills:
                    </FormLabel>
                    <Multiselect
                        onSelect={selectionChanged}
                        onRemove={selectionChanged}
                        name="particulars"
                        displayValue="name"
                        placeholder="Select Skills"
                        className="multiSelectContainer"
                        selectedValues={val}
                        options={skills}
                        ref={multiselectRef}>
                    </Multiselect>
                </FormGroup>
            </Form>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                <button className="button" onClick={() => createJobCandidate(input, val)}>Add</button>
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                {serverResponse}
            </div>
        </div>
    )
}