.PHONY: help
.DEFAULT_GOAL := help

install: ## Install requirements
	pip install -r requirements.txt

format: ## Run code formatters
	isort app tests
	black app tests

lint: ## Run code linters
	isort --check app tests
	black --check app tests
	flake8 app tests
	mypy app tests --explicit-package-bases

test:  ## Run tests with coverage
	pytest --cov

all:   ## run formatters, linters and tests
	isort app tests
	black app tests
	isort --check app tests
	black --check app tests
	flake8 app tests
	mypy app tests --explicit-package-bases
	pytest --cov

run:   ## Run program
	python3.10 main.py
