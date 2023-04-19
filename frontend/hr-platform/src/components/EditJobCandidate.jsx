import { useLocation } from "react-router-dom"
import { Button, Form, FormControl, FormGroup, FormLabel } from "react-bootstrap";
import { useEffect, useRef, useState } from "react";
import Multiselect from "multiselect-react-dropdown";
import Axios from "../Apis/Axios";

export default function EditJobCandidate(props) {

    const { state } = useLocation();
    const [skills, setSkills] = useState([]);
    const [input, setInput] = useState({
        name: "",
        dateOfBirth: "",
        contactNumber: "",
        email: "",
    });
    const [val, setVal] = useState([]);
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
        setInput({
            name: state.jobCandidate.name,
            dateOfBirth: state.jobCandidate.dateOfBirth,
            contactNumber: state.jobCandidate.contactNumber,
            email: state.jobCandidate.email
        })
        setVal(
            state.jobCandidate.skills
        )
        console.log(state)
    }, []);

    function handleChange(e) {
        const value = e.target.value;
        setInput({
            ...input,
            [e.target.name]: value
        });
        console.log(e.target.value);
    }

    function selectionChanged(selectedList, selectedItem) {
        console.log(selectedList)
        setVal(selectedList);
    }

    function resetValues() {
        multiselectRef.current.resetSelectedValues();
    }


    const editJob = async (input, val) => {

        let jobCandidateDto = {
            id: state.jobCandidate.id,
            name: input.name,
            dateOfBirth: input.dateOfBirth,
            contactNumber: input.contactNumber,
            email: input.email,
            skills: val

        };

        await Axios.put("/jobCandidate/" + state.jobCandidate.id, jobCandidateDto)
            .then(({ data }) => {
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

    }


    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                <h2>
                    Edit candidate
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
                    <Multiselect onSelect={selectionChanged}
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
                <Button variant="warning" onClick={() => editJob(input, val)}>Edit</Button>
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                {serverResponse}
            </div>

        </div>
    )
}