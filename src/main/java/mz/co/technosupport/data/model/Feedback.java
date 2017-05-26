package mz.co.technosupport.data.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Americo Chaquisse
 */
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private double rate;

    @Column
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    private Consumer consumer;

    @ManyToOne
    private Technitian technitian;

    @ManyToOne
    private Problem problem;

    @ManyToOne
    private Ticket ticket;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Technitian getTechnitian() {
        return technitian;
    }

    public void setTechnitian(Technitian technitian) {
        this.technitian = technitian;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
