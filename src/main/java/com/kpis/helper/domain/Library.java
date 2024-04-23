package com.kpis.helper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Library.
 */
@Entity
@Table(name = "library")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "library" }, allowSetters = true)
    private Set<Population> populations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "library" }, allowSetters = true)
    private Set<Collection> collections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Library id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Library name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public Library location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Population> getPopulations() {
        return this.populations;
    }

    public void setPopulations(Set<Population> populations) {
        if (this.populations != null) {
            this.populations.forEach(i -> i.setLibrary(null));
        }
        if (populations != null) {
            populations.forEach(i -> i.setLibrary(this));
        }
        this.populations = populations;
    }

    public Library populations(Set<Population> populations) {
        this.setPopulations(populations);
        return this;
    }

    public Library addPopulation(Population population) {
        this.populations.add(population);
        population.setLibrary(this);
        return this;
    }

    public Library removePopulation(Population population) {
        this.populations.remove(population);
        population.setLibrary(null);
        return this;
    }

    public Set<Collection> getCollections() {
        return this.collections;
    }

    public void setCollections(Set<Collection> collections) {
        if (this.collections != null) {
            this.collections.forEach(i -> i.setLibrary(null));
        }
        if (collections != null) {
            collections.forEach(i -> i.setLibrary(this));
        }
        this.collections = collections;
    }

    public Library collections(Set<Collection> collections) {
        this.setCollections(collections);
        return this;
    }

    public Library addCollection(Collection collection) {
        this.collections.add(collection);
        collection.setLibrary(this);
        return this;
    }

    public Library removeCollection(Collection collection) {
        this.collections.remove(collection);
        collection.setLibrary(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Library)) {
            return false;
        }
        return getId() != null && getId().equals(((Library) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Library{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
