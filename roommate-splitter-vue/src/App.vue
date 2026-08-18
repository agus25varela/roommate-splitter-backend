<template>
  <div style="padding: 20px; font-family: Arial, sans-serif;">
    <h1>Roommate Splitter</h1>

    <!-- Formulario para agregar gasto -->
    <div style="margin-bottom: 30px; border: 1px solid #ccc; padding: 15px; border-radius: 5px;">
      <h2>Agregar Gasto</h2>
      <input
          v-model="nuevoGasto.descripcion"
          placeholder="Descripción"
          style="display: block; margin-bottom: 10px; padding: 8px; width: 100%;"
      />
      <input
          v-model.number="nuevoGasto.monto"
          type="number"
          placeholder="Monto"
          style="display: block; margin-bottom: 10px; padding: 8px; width: 100%;"
      />
      <select
          v-model="nuevoGasto.quienPago"
          style="display: block; margin-bottom: 10px; padding: 8px; width: 100%;"
      >
        <option value="">Selecciona quién pagó</option>
        <option value="yo">Yo</option>
        <option value="roommate_a">Roommate A</option>
        <option value="roommate_b">Roommate B</option>
      </select>
      <input
          v-model="nuevoGasto.fecha"
          type="date"
          style="display: block; margin-bottom: 10px; padding: 8px; width: 100%;"
      />
      <button
          @click="crearGasto"
          style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer; border-radius: 3px;"
      >
        Agregar
      </button>
    </div>

    <!-- Tabla de gastos -->
    <div>
      <h2>Gastos</h2>
      <table style="width: 100%; border-collapse: collapse;">
        <thead>
        <tr style="background-color: #f2f2f2;">
          <th style="border: 1px solid #ddd; padding: 8px;">ID</th>
          <th style="border: 1px solid #ddd; padding: 8px;">Descripción</th>
          <th style="border: 1px solid #ddd; padding: 8px;">Monto</th>
          <th style="border: 1px solid #ddd; padding: 8px;">Quién Pagó</th>
          <th style="border: 1px solid #ddd; padding: 8px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="gasto in gastos" :key="gasto.id" style="border: 1px solid #ddd;">
          <td style="border: 1px solid #ddd; padding: 8px;">{{ gasto.id }}</td>
          <td style="border: 1px solid #ddd; padding: 8px;">{{ gasto.descripcion }}</td>
          <td style="border: 1px solid #ddd; padding: 8px;">${{ gasto.monto }}</td>
          <td style="border: 1px solid #ddd; padding: 8px;">{{ gasto.quienPago }}</td>
          <td style="border: 1px solid #ddd; padding: 8px;">{{ gasto.fecha }}</td>
        </tr>
        </tbody>
      </table>
      <p v-if="gastos.length === 0" style="color: #999; margin-top: 20px;">No hay gastos aún.</p>
    </div>

    <!-- Resumen de deudas -->
    <div style="margin-top: 30px; border: 1px solid #ff9800; padding: 15px; border-radius: 5px; background-color: #fff3e0;">
      <h2 style="color: #ff9800;">Resumen de Deudas</h2>
      <div v-if="balances.length === 0" style="color: #999;">
        Todos pagaron el mismo monto.
      </div>
      <div v-else>
        <p v-for="balance in balances" :key="`${balance.from}-${balance.to}`" style="margin: 10px 0; font-size: 16px;">
          <strong>{{ balance.from }}</strong> le debe <strong>${{ balance.amount.toFixed(2) }}</strong> a <strong>{{ balance.to }}</strong>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'App',
  setup() {
    const gastos = ref([])
    const balances = ref([])
    const nuevoGasto = ref({
      descripcion: '',
      monto: '',
      quienPago: '',
      fecha: ''
    })

    const API_URL = 'http://localhost:8080/api/gastos'

    // Cargar gastos al montar el componente
    onMounted(() => {
      cargarGastos()
      cargarBalances()
    })

    // Función para cargar gastos
    const cargarGastos = async () => {
      try {
        const response = await axios.get(API_URL)
        gastos.value = response.data
      } catch (error) {
        console.error('Error cargando gastos:', error)
      }
    }

    // Función para cargar balances
    const cargarBalances = async () => {
      try {
        const response = await axios.get(`${API_URL}/balances`)
        balances.value = response.data
      } catch (error) {
        console.error('Error cargando balances:', error)
      }
    }

    // Función para crear gasto
    const crearGasto = async () => {
      if (!nuevoGasto.value.descripcion || !nuevoGasto.value.monto || !nuevoGasto.value.quienPago || !nuevoGasto.value.fecha) {
        alert('Por favor completa todos los campos')
        return
      }

      try {
        await axios.post(API_URL, nuevoGasto.value)
        // Limpiar el formulario
        nuevoGasto.value = {
          descripcion: '',
          monto: '',
          quienPago: '',
          fecha: ''
        }
        // Recargar la lista y los balances
        cargarGastos()
        cargarBalances()
      } catch (error) {
        console.error('Error creando gasto:', error)
        alert('Error al crear el gasto')
      }
    }

    return {
      gastos,
      balances,
      nuevoGasto,
      crearGasto
    }
  }
}
</script>

<style scoped>
</style>