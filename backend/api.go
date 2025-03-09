package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

type WebAPIServer struct {
	url string
	db  Database
}

func NewWebAPIServer(url string, db Database) *WebAPIServer {
	return &WebAPIServer{
		url: url,
		db:  db,
	}
}

func WriteJSON(w http.ResponseWriter, status int, v any) error {
	w.Header().Add("Content-Type", "application/json")
	w.WriteHeader(status)
	return json.NewEncoder(w).Encode(v)
}

type ApiError struct {
	Error string `json:"error"`
}

type apiFunc func(http.ResponseWriter, *http.Request) error

func makeHttpHandlerFunc(f apiFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := f(w, r); err != nil {
			WriteJSON(w, http.StatusBadRequest, ApiError{Error: err.Error()})
		}
	}
}

func (s *WebAPIServer) Run() {
	router := mux.NewRouter()

	router.HandleFunc("/products", makeHttpHandlerFunc(s.handleGetProducts)).Methods("GET")
	router.HandleFunc("/product/{id}", makeHttpHandlerFunc(s.handleGetProduct)).Methods("GET")
	router.HandleFunc("/product", makeHttpHandlerFunc(s.handleCreateProduct)).Methods("POST")
	router.HandleFunc("/product", makeHttpHandlerFunc(s.handleUpdateProduct)).Methods("PUT")
	router.HandleFunc("/product/{id}", makeHttpHandlerFunc(s.handleDeleteProduct)).Methods("DELETE")

	log.Print("API server running on port: ", s.url)

	http.ListenAndServe(s.url, router)
}

func (s *WebAPIServer) handleGetProducts(w http.ResponseWriter, r *http.Request) error {
	page, err := strconv.Atoi(r.URL.Query().Get("page"))
	if err != nil || page < 1 {
		page = 1
	}
	limit, err := strconv.Atoi(r.URL.Query().Get("limit"))
	if err != nil || limit < 1 {
		limit = 10
	}
	offset := (page - 1) * limit

	searchText := r.URL.Query().Get("searchText")

	products, err := s.db.GetProducts(limit, offset, searchText)
	if err != nil {
		return err
	}

	return WriteJSON(w, http.StatusOK, products)
}

func (s *WebAPIServer) handleGetProduct(w http.ResponseWriter, r *http.Request) error {
	id, err := getIdFromRequest(r)
	if err != nil {
		return err
	}
	product, err := s.db.GetProduct(id)
	if err != nil {
		return err
	}
	return WriteJSON(w, http.StatusOK, product)
}

func (s *WebAPIServer) handleCreateProduct(w http.ResponseWriter, r *http.Request) error {
	newProduct := Product{}
	if err := json.NewDecoder(r.Body).Decode(&newProduct); err != nil {
		return err
	}
	id, err := s.db.CreateProduct(&newProduct)
	if err != nil {
		return err
	}
	return WriteJSON(w, http.StatusOK, id)
}

func (s *WebAPIServer) handleUpdateProduct(w http.ResponseWriter, r *http.Request) error {
	updateProduct := Product{}
	if err := json.NewDecoder(r.Body).Decode(&updateProduct); err != nil {
		return err
	}
	product, err := s.db.UpdateProduct(&updateProduct)
	if err != nil {
		return err
	}
	return WriteJSON(w, http.StatusOK, product)
}

func (s *WebAPIServer) handleDeleteProduct(w http.ResponseWriter, r *http.Request) error {
	id, err := getIdFromRequest(r)
	if err != nil {
		return err
	}
	if err := s.db.DeleteProduct(id); err != nil {
		return err
	}
	return WriteJSON(w, http.StatusOK, id)
}

func getIdFromRequest(r *http.Request) (int, error) {
	idStr := mux.Vars(r)["id"]
	id, err := strconv.Atoi(idStr)
	if err != nil {
		return id, fmt.Errorf("invalid id %s", idStr)
	}
	return id, nil
}
