```markdown
# 🔐 Security Password Manager

Um **gerenciador de senhas seguro** desenvolvido em **Java**, com suporte a múltiplos usuários, autenticação por senha (com `bcrypt`), autenticação em dois fatores (2FA), criptografia de senhas com AES, geração de senhas seguras e verificação de vazamentos de credenciais na web.

## 📦 Funcionalidades

### ✅ Autenticação Segura
- Autenticação com senha armazenada com hash seguro usando `BCrypt`.
- Autenticação em dois fatores (2FA) via TOTP.

### 🔐 Armazenamento Seguro
- Armazena credenciais (usuário, senha, URL, descrição) em um banco de dados local criptografado.
- As senhas são criptografadas com algoritmo **AES** de 256 bits.

### 🔍 Verificação de Vazamento
- Integração com o serviço **Have I Been Pwned** para verificar se uma senha foi comprometida.

### 🔒 Geração de Senhas Fortes
- Geração de senhas seguras com base em regras de complexidade configuráveis (comprimento, letras, números, símbolos).

### 👤 Suporte a Múltiplos Usuários
- Cada usuário define sua própria senha mestre (`MASTER_PASSWORD`) e chave de criptografia (`ENCRYPTION_KEY`).
- Esses dados são usados para proteger e acessar os dados do próprio usuário.

### 🖥️ Interface
- Interface baseada em **linha de comando (CLI)**, interativa e fácil de usar.

---

## 🧱 Estrutura do Projeto

```

Security-Password-Manager/
├── pom.xml                      # Gerenciamento de dependências Maven
├── database.db                  # Banco de dados local SQLite
├── totp.secret                  # Arquivo secreto para geração de TOTP
├── src/
│   └── main/
│       └── java/
│           ├── org/Main.java
│           ├── database/DataBaseSecurity.java
│           ├── model/Credential.java
│           ├── services/
│           │   ├── ServiceAuth.java
│           │   ├── ServiceCrypto.java
│           │   ├── CheckerBreach.java
│           │   └── GeneratorPassword.java
│           └── ui/ConsoleUI.java

````

---

## ⚙️ Como Executar

### ✅ Pré-requisitos

- Java 11+
- Maven 3.6+
- Internet (para verificação de vazamentos)

### 🔧 Configuração Dinâmica de Segurança

Ao iniciar o sistema, o **usuário cria um .env**
- Uma **senha mestre** (`MASTER_PASSWORD`), usada para autenticação.
- Uma **chave de criptografia** (`ENCRYPTION_KEY`), usada para encriptar/decriptar suas senhas.

> ⚠️ **Atenção:**  
> Essas informações são únicas por usuário e **não são compartilhadas**. O sistema armazena apenas as versões protegidas (por hash e criptografia).

### 🔧 Passos

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/seuusuario/Security-Password-Manager.git
   cd Security-Password-Manager
````

2. **Compile o projeto com Maven:**

   ```bash
   mvn clean install
   ```

3. **Execute a aplicação:**

   ```bash
   mvn exec:java -Dexec.mainClass="org.Main"
   ```

4. **Siga as instruções da interface para:**

   * Criar conta com senha mestre e chave de criptografia.
   * Ativar autenticação 2FA.
   * Armazenar e consultar credenciais com segurança.

---

## 🧪 Exemplo de Uso

* Defina sua senha mestre e chave de criptografia.
* Gere senhas fortes automaticamente.
* Armazene credenciais (email, senhas, sites, observações).
* Consulte ou edite credenciais com proteção criptográfica.
* Verifique se senhas já foram expostas em vazamentos.

---

## 🔒 Segurança

* Criptografia AES com chave individual por usuário.
* Senhas armazenadas com `BCrypt` e sal.
* Sistema de 2FA com base em TOTP.
* Arquitetura modular e segura para autenticação e armazenamento.

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

## 🤝 Contribuição

Contribuições são bem-vindas! Por favor, abra um *issue* ou envie um *pull request*.

```
