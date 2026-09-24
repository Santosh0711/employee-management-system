const API_URL = 'http://localhost:8080/employees';

// Get all employees
export const getAllEmployees = async () => {

    const response = await fetch(API_URL);

    if (!response.ok) {
        throw new Error('Failed to fetch employees');
    }

    return response.json();
};

// Add employee
export const addEmployee = async (employee) => {

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(JSON.stringify(error));
    }

    return response.json();
};

// Update employee
export const updateEmployee = async (id, employee) => {

    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(JSON.stringify(error));
    }

    return response.json();
};

// Delete employee
export const deleteEmployee = async (id) => {

    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Failed to delete employee');
    }

    return response.text();
};