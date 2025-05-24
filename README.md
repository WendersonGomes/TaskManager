
# Sistema de Gerenciamento de Tarefas

Aplicação web para **gerenciar tarefas**, utilizando tecnologias da plataforma Java EE (Jakarta EE).

---

## Tecnologias Utilizadas

- **Java EE (Jakarta EE)**
  - JSF (JavaServer Faces)
  - CDI (Contexts and Dependency Injection)
  - JPA (Java Persistence API)
- **Maven** (gerenciador de dependências e build)
- **PostgreSQL** (banco de dados relacional)
- **Tomcat 11** (servidor de aplicação)
- **IntelliJ IDEA**

---

## Funcionalidades Implementadas

- Cadastro de tarefas (número, título, descrição, responsável, prioridade, situação, deadline(prazo))
- Listagem de tarefas em tabela
- Atualizar (editar) e excluir tarefas
- Marcar tarefa como concluída
- Filtros por:
  - Número da tarefa
  - Título
  - Descrição
  - Responsável
  - Prioridade
  - Situação (em andamento ou concluída)
- Limpar tarefas por status (concluídas, em andamento ou todas)
- Mensagens de validação e erro com `<h:message>` e `<h:messages>`
- Layout simples e responsividade minima com CSS (não utilizei PrimeFaces)
- Separação de responsabilidades (model, service/controller, view)
- CDI para injeção de dependência
- JPA com PostgreSQL via `persistence.xml`
- Arquivos `pom.xml`, `beans.xml` e `web.xml` configurados corretamente

---

## Como Executar Localmente

### Requisitos

- Java JDK 21
- PostgreSQL instalado
- IntelliJ
- Apache Tomcat 11
- Maven

---

### 1. Clone o projeto

---

### 2. Criar o banco de dados PostgreSQL

Acesse o PostgreSQL e crie um banco de dados chamado TaskManagerDB (ou o nome que preferir)

---

### 3. Configuração do `persistence.xml`

Localize o arquivo:  
`src/main/resources/META-INF/persistence.xml`  
E use o seguinte conteúdo (ou confirme que está igual):

:warning: Lembre-se de alterar o usuario, senha e nome do banco :warning:

```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

  <persistence-unit name="tasksPU">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <class>com.esig.taskmanager.model.Task</class>
    <properties>
      <!-- JDBC Configuration -->
      <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/TaskManagerDB"/> <!-- insira a porta caso seja diferente junto ao nome do banco -->
      <property name="jakarta.persistence.jdbc.user" value="postgres"/> <!-- insira seu usuario -->
      <property name="jakarta.persistence.jdbc.password" value="senha"/> <!-- insira sua senha -->
      <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>

      <!-- Hibernate Configuration -->
      <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
      <property name="hibernate.hbm2ddl.auto" value="update"/>
      <property name="hibernate.show_sql" value="true"/>
      <property name="hibernate.format_sql" value="true"/>
    </properties>
  </persistence-unit>
</persistence>
```

---

### 4. Instalar dependências do Maven

No IntelliJ:

- Clique com o botão direito no `pom.xml` > **"Add as Maven Project"**
- Abra o painel Maven (View > Tool Windows > Maven) e clique em **refresh**

**Apenas caso isso não tenho sido feito de maneira automatica**

---

### 5. Criar o artifact

1. Vá em **File > Project Struture > Artifacts** > clique no +, Web Application: Exploded

### 6. Configurar o Tomcat

1. Vá em **File > Settings > Build, Execution, Deployment > Application Servers**
  - Clique no **+**, escolha **Tomcat Server**, e selecione a pasta do Tomcat 11
2. Vá em **Run > Edit Configurations**

  - Clique no **+ > Tomcat Server > Local**
  - Em **Before launch**, clique em +, selecione Build Artifacts e TaskManager:war exploded
  - Em **Deployment**, clique no `+` > **Artifact** > selecione `Tomcat 11.0.7`

3. Clique em **Apply** e depois em **Run**

---

### 6. Acessar a aplicação

Após iniciar o Tomcat, acesse:

```
 http://localhost:8080/TaskManager_war_exploded/
```

---
