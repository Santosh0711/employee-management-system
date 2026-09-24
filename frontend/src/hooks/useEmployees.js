import { useEffect, useState } from 'react';

import {
    getAllEmployees,
    addEmployee,
    updateEmployee,
    deleteEmployee
} from '../services/employeeService';

function useEmployees() {

    const [employees, setEmployees] = useState([]);

    const [loading, setLoading] = useState(true);

    const [error, setError] = useState('');

    // Get all employees
    const fetchEmployees = async () => {

        try {

            setLoading(true);

            setError('');

            const data = await getAllEmployees();

            setEmployees(data);

        } catch (error) {

            console.error('Error fetching employees:', error);

            setError(
                'Unable to load employees. Please make sure the backend is running.'
            );

        } finally {

            setLoading(false);
        }
    };

    // Add employee
    const createEmployee = async (employee) => {

        try {

            const data = await addEmployee(employee);

            await fetchEmployees();

            return data;

        } catch (error) {

            console.error('Error adding employee:', error);

            throw error;
        }
    };

    // Update employee
    const editEmployee = async (id, employee) => {

        try {

            const data = await updateEmployee(id, employee);

            await fetchEmployees();

            return data;

        } catch (error) {

            console.error('Error updating employee:', error);

            throw error;
        }
    };

    // Delete employee
    const removeEmployee = async (id) => {

        try {

            const message = await deleteEmployee(id);

            await fetchEmployees();

            return message;

        } catch (error) {

            console.error('Error deleting employee:', error);

            throw error;
        }
    };

    // Load employees when the hook is first used
    useEffect(() => {

        fetchEmployees();

    }, []);

    return {
        employees,
        loading,
        error,
        fetchEmployees,
        createEmployee,
        editEmployee,
        removeEmployee
    };
}

export default useEmployees;