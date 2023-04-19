import { useState } from "react";
import Axios from "../Apis/Axios";
import { Form, FormGroup, FormControl, FormLabel } from "react-bootstrap";

export default function AddSkill(props) {


    const [input, setInput] = useState({
        name: ""
    });


    const createSkill = async (input) => {

        let skillDto = {
            name: input.name
        };

        await Axios.post("/skill", skillDto, {
            headers: {
                Accept: "application/json",
            },
        }).then(({ data }) => {
            setServerResponse("Uspesno izvresno");
            setInput({
                name: ""
            });
            console.log(data);
        }).catch(error => {
            setServerResponse("Neuspesno izvresno");
            console.log(error.response.data.error);
        });
    };

    function handleChange(e) {
        const value = e.target.value;
        setInput({
            ...input,
            [e.target.name]: value
        });
        console.log(e.target.value);
    }

    const [serverResponse, setServerResponse] = useState("");



    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                <h2>
                    Add skill
                </h2>
            </div>
            <Form className="form">
                <FormGroup >
                    <FormLabel >
                        Name:
                    </FormLabel>
                    <FormControl type="text" name="name" value={input.name} onChange={handleChange}></FormControl>
                </FormGroup>
            </Form>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                <button className="button" onClick={() => createSkill(input)}>Add</button>
            </div>

            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
                {serverResponse}
            </div>

        </div>
    )
}