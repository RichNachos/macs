.PHONY: help
.DEFAULT_GOAL := help

install: ## Install requirements
	pip install -r requirements.txt

build: ## run Build.py
	python build.py

run: ## run Run.py
	python run.py

format: ## Run code formatters
	isort build.py run.py tests
	black build.py run.py tests

lint: ## Run code linters
	isort --check build.py run.py tests
	black --check build.py run.py tests
	flake8 build.py run.py tests

all:
	isort build.py run.py tests
	black build.py run.py tests
	isort --check build.py run.py tests
	black --check build.py run.py tests
	flake8 build.py run.py tests
