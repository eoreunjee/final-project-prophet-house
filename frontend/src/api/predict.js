// src/api/predict.js
import axios from 'axios';

export function fetchPredictionBar(payload) {
  return axios.post('http://localhost:8000/predict_bar', payload, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

export function fetchPrediction(payload) {
  return axios.post('http://localhost:8000/predict', payload, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}